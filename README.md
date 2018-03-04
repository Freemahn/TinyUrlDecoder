# TinyUrlDecoder
Upwork project. Purpose is to encode/decode atlassian's tinyUrls to ids and back, with simple GUI(JavaFX)  
Example of tinyUrl = **/x/pw7H**  
Example of id = **13045415**  
This solution is based on the algorithm of [Mircea Vutcovici](https://community.atlassian.com/t5/Confluence-questions/What-is-the-algorithm-used-to-create-the-quot-Tiny-links-quot/qaq-p/186555 )

## How to build it
`gradle fatJar`  
In {Project.root}/build/libs/ you will find tiny-url-decoder-all-1.0-SNAPSHOT.jar

## How to run it
`java -jar tiny-url-decoder-all-1.0-SNAPSHOT.jar`  
Or just double click on jar file. There will be GUI provided.
