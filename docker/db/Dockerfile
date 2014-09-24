FROM postgres:9.3

CMD ["postgres"]
# CMD psql -U postgres postgres < /tmp/scripts/schema.sql
ENV PGDATA /data
ENTRYPOINT ["/docker-entrypoint.sh"]
EXPOSE 5432

