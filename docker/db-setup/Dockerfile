FROM postgres:9.3 

ADD scripts /tmp/scripts
CMD psql -U postgres -h db postgres < /tmp/scripts/schema.sql
CMD echo "select * from event.events" | /usr/lib/postgresql/9.3/bin/psql -U postgres -h db postgres
