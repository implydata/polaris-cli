## Description
The Polaris command-line interface (polaris-cli) is a set of commands used to create and manage Imply SaaS resources. 
E.g.,  create a table, upload a file, create a batch job, and query based on the data.

## Requirements
- Java 1.8 installed
- An API Client is created

## Installation
- Download [polaris-cli-1.0-all.jar](https://github.com/shshen/polaris-cli/releases/download/v0.1/polaris-cli-1.0-all.jar) to your local,E.g.,~/imply 
- Create alias: E.g., “alias polaris="java -jar ~/imply/polaris-cli-1.0-all.jar"

## Usage
Polaris service configuration
```shell
polaris config -e=eng -o=shane-aug10 --client_id=test_client --client_secret=<SECRET>
```
The sub-commands help for a service, e.g., create a detail table:
```shell
polaris tables detail -h
Usage: polaris tables detail [-h] [--verbose] [-a=<authorization>]
                             [-d=<description>] [-e=<environment>]
                             [-g=<partitioningGranularity>] [-k=<apiKey>]
                             -n=<name> [-o=<organization>] [-t=<token>]
                             [-v=<version>] [-c=<clusteringColumns>]...
                             [-S=<String=String>]...
Create a detail table
  -a, --authorization=<authorization>
                            Enum values: token, basic
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
  -v, --version=<version>   Version number of the table
      --verbose             Enable to print debug info
Authorization section
  -k, --apiKey=<apiKey>     The apiKey to a Polaris API
  -t, --token=<token>       The access token to a Polaris API
```