Distributed SFTP consumer application using Spring Integration
==============================================================

# Overview

Spring Integration greatly simplifies the task of creating an application that pulls files from remote locations such as an SFTP server.  There are cases where a remote file consumer is part of a distributed application and there will be more than one process trying to pull the files from the same location.  This example project shows how it is possible to use an external shared resource like MongoDB to create a lock so that only a single consumer will grab the remote file.


# How to Run the Sample

To run the example code in this project it is necessary to have an available MongoDB server and an SFTP server.

The default SFTP setup:

    host: localhost
    user: ftpuser
    password: ftppass
    remote directory: pickup/
    local directory : incoming/


The default MongoDB setup:

  host: localhost


Run this in the home directory of the ftpuser account to create a test file in the pickup directory:

    $ mkdir pickup
    $ echo "Example file" > pickup/example.txt


The sample application can be found in the following JUnit class:
com.harpatec.springint_sftp_dist.SftpDistTest


