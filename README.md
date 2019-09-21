# hash-handler

This service expose 2 REST APIs:
1. /messages takes a message (a string) as a POST and returns the SHA256 hash digest of
that message (in hexadecimal format)

Example: 
curl -X POST -H "Content-Type: application/json" -d "{\"message\":\"foo\"}" http://localhost:8080/messages

2. /messages/<hash> is a GET request that returns the original message. A request to a
   non-existent<hash> should return a 404 error.
   
Example: 
 curl http://localhost:8080/messages/2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae
 
The service is integrated with AWS DynamoDB as its DB, before running it locally you should verify you got an IAM user 
with DDB read and write access. Since the service uses "DefaultAWSCredentialsProviderChain", make sure to configure your 
credentials by the AWS documentation on https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html. 
You can also run it on EC2 that has IAM Role with those permissions.
