# Introduction #

Release engineering shouldn't be hard, and indeed isn't, but if it's not documented it **will** go wrong one day.

## Create Tag ##

```
svn copy https://jenatool.googlecode.com/svn/trunk/ https://jenatool.googlecode.com/svn/tags/{version} -m "Declaring {version} release"
```


## Create Update Site ##

To create/install a build to the update site.

  1. Open site.xml
  1. Run the "Build All" command to create all the necessary artifacts
  1. Open the S3 bucket at http://jenatool.s3.amazonaws.com/
  1. Copy the following files **only** to the S3 bucket:
    * `features/*`
    * `plugins/*`
    * `site.xml`
  1. Specifically, do not copy artifacts.xml and content.xml.
  1. Zip up the same contents as were copied to S3 and name them:
    * `org.johnstonshome.jenatool.{version}.zip`
  1. Add the zip file to the Google Code project Downloads.

## Update Project Plan ##

Ensure the ProjectPlan is updated to denote the changes in this release, mark the release _complete_ and identify the next release as _current_. Ensure any outstanding issues targeted at this release are pushed forward.