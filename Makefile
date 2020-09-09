run: compile
	make compile
	java -cp jars/gson-2.8.5.jar:bin/ cs1302.gallery.GalleryDriver

compile:
	javac -cp jars/gson-2.8.5.jar:bin/ -d bin/ src/cs1302/gallery/*.java

clean:
	rm bin/cs1302/gallery/*.class

