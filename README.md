## Description
The Polaris command-line interface (polaris-cli) is a set of commands used to create and manage Imply SaaS resources. 
E.g.,  create a table, upload a file, create a batch job, and query based on the data.

## Requirements
- Java 1.8 installed
- An API Client is created

## Installation
- Download [polaris-cli-1.0-jar-with-dependencies.jar](https://github.com/shshen/polaris-cli/releases/download/v0.1/polaris-cli-1.0-jar-with-dependencies.jar) to your local,E.g.,~/imply 
- Create alias: E.g., “alias polaris="java -jar ~/imply/polaris-cli-1.0-jar-with-dependencies.jar"

## Usage
You can get help on the command line to see the supported services,
```shell
polaris -h
Usage: polaris [-h] [COMMAND]
  -h, --help   Display this help and exit
Commands:
  login       Obtain an API token and save it for later use
  table       Manage tables and their schemas
  file        Upload, list, and delete files
  connection  Manage connections to external sources of data
  event       Push events to a Polaris connection
  job         Manage jobs in Imply Polaris
  sql         Start running SQL queries against the data
  datacube    Manage data cubes
  dashboard   Manage dashboards
  user        Manage user and it's access to protected data and resources
  project     Manage Imply Polaris resources
```
Login to Polaris service
```shell
polaris login -e=eng -o=shane-aug10 --client_id=test_client --client_secret=<SECRET>
```
The sub-commands help for a service, e.g., create a detail table:
```shell
polaris table detail -h
Usage: polaris table detail [-h] [--verbose] [-d=<description>]
                            [-e=<environment>] [-g=<partitioningGranularity>]
                            -n=<name> [-o=<organization>] [-t=<token>]
                            [-v=<version>] [-c=<clusteringColumns>]...
                            [-S=<String=String>]...
Create a detail table
  -c, --columns=<clusteringColumns>
                            Cluster columns
  -d, --description=<description>
                            Table description.
  -e, --environment=<environment>
                            Enum values: eng, staging, prod
  -g, --granularity=<partitioningGranularity>
                            Enum values: hour, day, week, month, year
  -h, --help                Display this help and exit
  -n, --name=<name>         Table name
  -o, --organization=<organization>
                            Organization name
  -S, --schema=<String=String>
                            Schema map
  -t, --token=<token>       The access token to a Polaris API
  -v, --version=<version>   Version
      --verbose             Enable to print debug info
```