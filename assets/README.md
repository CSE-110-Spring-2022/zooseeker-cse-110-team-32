# ZooSeeker-Assets
Static assets for the ZooSeeker app. 

## Zoo Graph Format

* sample_graph.json: sample graph node (ids and labels) and edge (source node, target node, and edge weight) information, in JSON format; see MainActivity for an example of this being read by JSONImporter
* sample_graph.dot: contains the same information as described above, but in DOT (a graph description language) format, in case you would like to use other libraries that require a different input file type. (TO BE ADDED SOON!)

## Node Data Format

* sample_exhibits.json: an array of objects, each of which contains information about whether the node is an animal exhibit, intersection, or gate (entrance/exit), as well as additional information about each node ("tags")
  * Refer to Lab 5 for more information on working with this data format.

## Example Code
* MainActivity.java: an example of loading the sample graph data and running Djikstra's Algorithm on it to find the shortest path (DijkstraShortestPath).
  * Note that you can use a different path algorithm. 
  * Although we use the JGraphT Java library (https://jgrapht.org/) in the example code, you can use something else if you would like to.

### Dependencies
If choosing to use JGraphT, as in the provided example, the versions should be 1.5.0 or above:

implementation 'org.jgrapht:jgrapht-core:1.5.0'

implementation group: 'org.jgrapht', name: 'jgrapht-io', version: '1.5.0'
