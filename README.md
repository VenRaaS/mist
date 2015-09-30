### TOC
* [Goal](#goal)
* [Why](#why)
* [Status](#status)
* [Getting started](#getting-started)

# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

![](https://drive.google.com/uc?id=0B78KhWqVkVmtU0tJTGVHaHJTN0E)

# Why
It's highly cost-efficient that put data in PaaS well-designed storage instead of storing data in self-setup VM.

<img src="https://drive.google.com/uc?id=0B78KhWqVkVmtTkFmOXN4cEpka2s" width="300">   <img src="https://drive.google.com/uc?id=0B78KhWqVkVmtVXFUOGdWbG1mcVk" width="300">

# Status
## Azure BLOB
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
* Notes: please specify `-Xmx${1.5 * $FileSIZE}` if OutOfMemoryError occurs.

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
