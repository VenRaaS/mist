# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

# Status
## Azure
* parallel **upload** with progress
* parallel **download** with progress
* simple file prefix pattern match include upload and download

![](https://lh3.googleusercontent.com/fjlXdRt8rWyUhe6VDVZrQBwvC1xiv2LmAo7mtugtd5rZADFrp2AZl7b4z9LqYohi3q2Cm0PKnz4nMP4=w1576-h693)

![](https://lh6.googleusercontent.com/whwRhs4x-YOfgJhiG8n-BRhiuK0lbK8eDTcIPAQc0j0CucQThWR11hlUYnXKPqmXnwzVD_rsV_A_Uys=w1573-h680)

# Getting Started
## Usage
### Prequisite
* JRE 1.7+
* Available memory > 1.5 * $FileSize

### Upload a single file from local to cloud
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn up
```
### Download a single file from cloud to local
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn down
```
### Upload a bunch of files with a common prefix pattern
* the pattern should be doulbe-quote **"** surrounded
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp "recomdlog_*" -fn up
```
