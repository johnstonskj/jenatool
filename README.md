![](https://raw.githubusercontent.com/johnstonskj/jenatool/master/org.johnstonshome.jenatool.ui/icons/jenatool-logo-small.png)

# Introduction 

JenaTool is a set of Eclipse UI extensions for helping developers work with RDF data and SPARQL queries using the [Jena Semantic Web Framework](http://jena.sourceforge.net/documentation.html).

# Features

The following is a high level view of the features planned for !JenaTool, specific wiki pages will be created to describe some features in more detail. For more on the overview of the project see the [Vision wiki topic](../../wiki/Vision).

* _Jena Perspective_ - contains the _Jena Explorer View_ and _SPARQL Results View_.
* _Jena Explorer View_ - to list and store connections to Jena repositories, currently only TDB is supported as a connection type (see [JenaExplorer wiki](../../wiki/JenaExplorer)).
  * Allows adding a new connection, deleting existing connections and marking connections as "default" for some actions.
* _SPARQL Results View_ - a text view that displays results from running SPARQL queries (see [SparqlExecution wiki](../../wiki/SparqlExecution)).
  * Will append results to the current output to allow aggregating results, also displays timings for queries.
  * Results view can be cleared
* _File Types_ - registered file types for SPARQL, N-Triples, Turtle, N3 and RDF/XML with associated editors (see [ContentTypes wiki](../../wiki/ContentTypes) and [RdfImport wiki](../../wiki/RdfImport)).
  * SPARQL file type supports a context menu to run the query, using the default connection as a context.
  * RDF file types support a context menu to import the content into one of the connections.
* _Properties_ - configuration to denote the default behavior of the views:
  * For example, selecting the default output representation for DESCRIBE and CONSTRUCT query forms (see [PropertyPages wiki](../../wiki/PropertyPages)).

# Download

- Use the update site at <http://jenatool.s3.amazonaws.com/> to obtain the latest release.
- Checkout from the commandline,
- or download either the [master](https://github.com/johnstonskj/jenatool/archive/master.zip) or a [release](https://github.com/johnstonskj/jenatool/releases) and build the sources yourself

# Plan 

The initial [Project plan wiki](../../wiki/ReleaseNotes) is published on the wiki, more details to be added in the near future.
