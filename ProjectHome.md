# Introduction #

JenaTool is a set of Eclipse UI extensions for helping developers
work with RDF data and SPARQL queries using the [Jena Semantic Web Framework](http://jena.sourceforge.net/documentation.html).

# Features #

The following is a high level view of the features planned for JenaTool, specific wiki pages will be created to describe some features in more detail. For more on the overview of the project see the [Vision](Vision.md) wiki topic.

  * _Jena Perspective_ - contains the _Jena Explorer View_ and _SPARQL Results View_.
  * _Jena Explorer View_ - to list and store connections to Jena repositories, currently only TDB is supported as a connection type (see JenaExplorer].
    * Allows adding a new connection, deleting existing connections and marking connections as "default" for some actions.
  * _SPARQL Results View_ - a text view that displays results from running SPARQL queries (see SparqlExecution).
    * Will append results to the current output to allow aggregating results, also displays timings for queries.
    * Results view can be cleared
  * _File Types_ - registered file types for SPARQL, N-Triples, Turtle, N3 and RDF/XML with associated editors (see ContentTypes and RdfImport).
    * SPARQL file type supports a context menu to run the query, using the default connection as a context.
    * RDF file types support a context menu to import the content into one of the connections.
  * _Properties_ - configuration to denote the default behavior of the views:
    * For example, selecting the default output representation for DESCRIBE and CONSTRUCT query forms (see PropertyPages).

# Download #

There are three methods to retrieve the JenaTool Eclipse plugins:

  1. Use the update site at http://jenatool.s3.amazonaws.com/ to obtain the latest release.
  1. Download one of the archived update sites corresponding to each release on the [Downloads tab](http://code.google.com/p/jenatool/downloads/list) above.
  1. Checkout and build the sources yourself, see the [Source tab](http://code.google.com/p/jenatool/source/checkout) for more details.

# Plan #

The initial ProjectPlan is published on the wiki, more details to be added in the near future.