docker run -it \
--name fitmate-mysql \
-e MYSQL_ROOT_PASSWORD="fitmate" \
-e MYSQL_USER="fitmate" \
-e MYSQL_PASSWORD="fitmate" \
-e MYSQL_DATABASE="fitmate" \
-p 3306:3306 \
-d mysql