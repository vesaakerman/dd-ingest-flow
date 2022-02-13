dd-ingest-flow
===========

<!-- Remove this comment and extend the descriptions below -->


SYNOPSIS
--------

    dd-ingest-flow { server | check }

DESCRIPTION
-----------

Ingests auto-deposits into a Dataverse instance


ARGUMENTS
---------

        positional arguments:
        {server,check}         available commands
        
        named arguments:
        -h, --help             show this help message and exit
        -v, --version          show the application version and exit

EXAMPLES
--------

<!-- Add examples of invoking this module from the command line or via HTTP other interfaces -->


INSTALLATION AND CONFIGURATION
------------------------------
Currently this project is built as an RPM package for RHEL7/CentOS7 and later. The RPM will install the binaries to
`/opt/dans.knaw.nl/dd-ingest-flow` and the configuration files to `/etc/opt/dans.knaw.nl/dd-ingest-flow`.

For installation on systems that do no support RPM and/or systemd:

1. Build the tarball (see next section).
2. Extract it to some location on your system, for example `/opt/dans.knaw.nl/dd-ingest-flow`.
3. Start the service with the following command
   ```
   /opt/dans.knaw.nl/dd-ingest-flow/bin/dd-ingest-flow server /opt/dans.knaw.nl/dd-ingest-flow/cfg/config.yml 
   ```

BUILDING FROM SOURCE
--------------------
Prerequisites:

* Java 8 or higher
* Maven 3.3.3 or higher
* RPM

Steps:

    git clone https://github.com/DANS-KNAW/dd-ingest-flow.git
    cd dd-ingest-flow 
    mvn clean install

If the `rpm` executable is found at `/usr/local/bin/rpm`, the build profile that includes the RPM packaging will be activated. If `rpm` is available, but at a
different path, then activate it by using Maven's `-P` switch: `mvn -Pprm install`.

Alternatively, to build the tarball execute:

    mvn clean install assembly:single
