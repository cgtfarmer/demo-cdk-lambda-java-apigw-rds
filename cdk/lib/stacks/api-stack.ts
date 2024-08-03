import { homedir } from 'os';
import { join } from 'path';
import { Construct } from 'constructs';
import { BundlingOutput, Duration, Stack, StackProps } from 'aws-cdk-lib';
import { Code, Function, ParamsAndSecretsLayerVersion, ParamsAndSecretsLogLevel, ParamsAndSecretsVersions, Runtime } from 'aws-cdk-lib/aws-lambda';
import { CorsHttpMethod, HttpApi, HttpMethod } from 'aws-cdk-lib/aws-apigatewayv2';
import { HttpLambdaIntegration } from 'aws-cdk-lib/aws-apigatewayv2-integrations';
import { Port, Vpc } from 'aws-cdk-lib/aws-ec2';
import { DatabaseProxy } from 'aws-cdk-lib/aws-rds';
import { ISecret } from 'aws-cdk-lib/aws-secretsmanager';

interface ApiStackProps extends StackProps {
  vpc: Vpc;

  rdsProxy: DatabaseProxy;

  rdsSecret: ISecret;

  rdsPort: string;

  rdsDbName: string;
}

export class ApiStack extends Stack {

  constructor(scope: Construct, id: string, props: ApiStackProps) {
    super(scope, id, props);

    const paramsAndSecrets = ParamsAndSecretsLayerVersion.fromVersion(
      ParamsAndSecretsVersions.V1_0_103,
      {
        cacheSize: 500,
        logLevel: ParamsAndSecretsLogLevel.DEBUG,
      }
    );

    const demoLambda = new Function(this, 'DemoLambda', {
      vpc: props.vpc,
      runtime: Runtime.JAVA_17,
      handler: 'com.cgtfarmer.demo.Handler',
      code: Code.fromAsset(join(__dirname, '../../../src/user-service'), {
        bundling: {
          image: Runtime.JAVA_17.bundlingImage,
          user: 'root',
          command: [
            '/bin/sh',
            '-c',
           'mvn clean install -Dmaven.test.skip=true && cp /asset-input/target/demo-cdk-lambda-java-0.0.1.jar /asset-output/'
          ],
          // Mounting local ~/.m2 repo to avoid re-downloading all the dependencies
          volumes: [
            {
              hostPath: join(homedir(), '.m2/repository'),
              containerPath: '/root/.m2/repository/'
            }
          ],
          outputType: BundlingOutput.ARCHIVED
        }
      }),
      environment: {
        DB_JDBC_URL: `jdbc:postgresql://${props.rdsProxy.endpoint}:${props.rdsPort}/${props.rdsDbName}`,
        // UNSAFE:
        // RDS_USERNAME: props.rdsSecret.secretValueFromJson('username').toString(),
        // RDS_PASSWORD: props.rdsSecret.secretValueFromJson('password').toString(),
        DB_CREDS_SECRET_ID: props.rdsSecret.secretName,
        DB_HOSTNAME: props.rdsProxy.endpoint,
        DB_PORT: props.rdsPort,
        DB_DATABASE_NAME: props.rdsDbName,
        DB_CHANGELOG_FILE: 'db/db.changelog-root.yaml',
      },
      memorySize: 1024,
      timeout: Duration.seconds(30),
      paramsAndSecrets: paramsAndSecrets,
    });

    props.rdsSecret.grantRead(demoLambda);
    props.rdsProxy.grantConnect(demoLambda);
    // This direction is incorrect due to causing cyclic dependencies
    // props.rdsProxy.connections.allowFrom(demoLambda, Port.POSTGRES);
    demoLambda.connections.allowTo(props.rdsProxy, Port.POSTGRES);

    const demoLambdaIntegration =
      new HttpLambdaIntegration('DemoLambdaIntegration', demoLambda);

    const httpApi = new HttpApi(this, 'HttpApi', {
      createDefaultStage: false,
      corsPreflight: {
        allowHeaders: ['Authorization'],
        allowMethods: [CorsHttpMethod.ANY],
        allowOrigins: ['*'],
        maxAge: Duration.days(10),
      },
    });

    httpApi.addStage('DefaultStage', {
      stageName: '$default',
      autoDeploy: true,
      throttle: {
        burstLimit: 2,
        rateLimit: 1,
      }
    });

    httpApi.addRoutes({
      path: '/users',
      methods: [HttpMethod.GET, HttpMethod.POST],
      integration: demoLambdaIntegration,
    });

    httpApi.addRoutes({
      path: '/users/{id}',
      methods: [HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE],
      integration: demoLambdaIntegration,
    });
  }
}
