## Description
The Polaris command-line interface (polaris-cli) is a set of commands used to create and manage Imply SaaS resources. 
E.g.,  create a table, upload a file, create a batch job, and query data.

## Requirements
- Java 1.8+ installed

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
Usage: polaris tables detail (-n=<name> [-v=<version>]
                             [-g=<partitioningGranularity>]
                             [-c=<clusteringColumns>]... [-d=<description>]
                             [-S=<String=String>]...) [[-h] -e=<environment>
                             -o=<organization> [--verbose] [--output=<output>]
                             [-a=<authorization>] -t=<token> [-k=<apiKey>]]
Description:
Create a detail table

Table detail options:
  -c, --columns=<clusteringColumns>
                            Cluster columns
  -d, --description=<description>
                            Table description.
  -g, --granularity=<partitioningGranularity>
                            Enum values: hour, day, week, month, year
  -n, --name=<name>         Table name
  -S, --schema=<String=String>
                            Schema map
  -v, --version=<version>   Version number of the table
General options:
  -a, --authorization=<authorization>
                            Enum values: token, basic
  -e, --environment=<environment>
                            Enum values: eng, staging, prod
  -h, --help                Display this help and exit
  -k, --apiKey=<apiKey>     The apiKey to a Polaris API
  -o, --organization=<organization>
                            Organization name
      --output=<output>     Enum values: json, table
  -t, --token=<token>       The access token to a Polaris API
      --verbose             Enable to print debug info
```