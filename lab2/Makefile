jar:
     mvn clean compile assembly:single
build: jar
	docker build -t asemchenko/parallel-lab-1 .
run: build
	docker run --rm --mount type="bind,source=C:\Users\asem\Desktop\KPI_SEMESTER_3\parallel\studying-parallel-programming\lab1\target,target=/result" asemchenko/parallel-lab-1 8 3600 /result

clear:
	mvn clean
	docker rmi asemchenko/parallel-lab-1