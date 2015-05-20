# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

# Why
It's highly cost-efficient 
Instead of storing data in self-setup VM.

![](https://drive.google.com/uc?id=0B78KhWqVkVmtTkFmOXN4cEpka2s)

![](https://drive.google.com/uc?id=0B78KhWqVkVmtVXFUOGdWbG1mcVk)

# Status
## Azure
* parallel **upload** with progress
* parallel **download** with progress
* simple file prefix pattern match include upload and download

# Getting Started
## Prequisite
* JRE 1.7+
* Available memory > 1.5 * $FileSize

## Self-executing Jar
### Download
[mist-self-executing.jar](https://github.com/VenRaaS/mist/blob/master/mist/target/mist-self-executing.jar?raw=true)

### Build package with Maven 
`mvn clean package`
* the Jar file will be generate under `mist\target`

## Usage
### Upload a single file from local to cloud
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn up
```
### Download a single file from cloud to local
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp recomdlog_20150115 -fn down
```
### Upload a bunch of files with a common prefix pattern
* the prefix pattern should be **doulbe-quote surrounded, e.g. "recomdlog_*"**
```
java -jar mist-self-executing.jar -ak <account key> -an <account name> -c <container> -ffp "recomdlog_*" -fn up
```
