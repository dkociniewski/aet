# AET Upgrade Notes

If you are upgrading AET from the previous version, here are notes that will help you do all 
necessary configuration changes that were introduced in comparison to previously released AET version.

You may see all changes in the [Changelog](https://github.com/Cognifide/aet/blob/master/CHANGELOG.md).

## Unreleased

### [PR-326](https://github.com/Cognifide/aet/pull/326) Upgrade OSGI annotations to 6.0.0 version

With the update osgi annotations to 6.0.0 version we had to change a little bit variable names. Currently, your config could has old names and you have to update them. Please follow instruction below:

1. Open `aet\osgi-dependencies\configs\src\main\resources`

2. In all files in this directory change variable names in way presented below:
```
MongoURI -> mongoURI
AllowAutoCreate -> allowAutoCreate
report-domain -> reportDomain
jxm-url -> jxmUrl
```

For example in `com.cognifide.aet.vs.mongodb.MongoDBClient.cfg`:
```
MongoURI=mongodb://localhost
AllowAutoCreate=true
```

Please replace it by:
```
mongoURI=mongodb://localhost
allowAutoCreate=true
```
