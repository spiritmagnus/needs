#!/bin/sh
psql -U postgres -h db postgres -f /tmp/scripts/schema.sql
psql -U postgres -h db postgres -f /tmp/scripts/testdata.sql
