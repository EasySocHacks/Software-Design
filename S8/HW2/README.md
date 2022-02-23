## Run project
To run this project you can do it either with [docker](https://www.docker.com/) or manually:
<details>
<summary>Docker</summary>

From [root](/):
* Execute ```docker-compose build```
* Execute ```docker-compose up```

It might take **SOME** times 

</details>

<details>
<summary>Manually</summary>

* Run mongodb on port 27017. For example by starting docker image by executing ```docker run -p 27017:27017 mongo```
* Run spring boot project by executong ```gradle bootRun```

</details>