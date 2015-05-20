# Goal
Mist is a client tool takes charge of Upload/Download file Into/From PaaS storage, i.e. Azure storage, AWS S3 and Google cloud storage, efficiently with progress status.

# Status
## Azure
* parallel **upload** with progress
* parallel **download** with progress
* simple file prefix pattern match include upload and download

![](https://lh3.googleusercontent.com/t-VHH6Xm051znx-UXEXO2h_AVwXIVw0vj3jjz9x_Zx33Ft_05ZEds-WyJ7Jo3uPVyMxhV5PiFZ0YiJY=w1576-h693)
![](https://lh4.googleusercontent.com/zURMMpv99s3qhGliCGvW1LXmladwmKSz6z9DDbiTplFfetNmb8_sDgcgnkT9qtgYqTFf9HzJjmivAu4=w1576-h693)

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
