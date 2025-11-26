#!/bin/bash
mongosh -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" admin <<EOF
    use foo;
    db.createUser({user: "$MONGO_INITDB_ROOT_USERNAME", pwd: "$MONGO_INITDB_ROOT_PASSWORD", roles: ["readWrite"],});
    db.createCollection('properties');
    db.properties.insertOne({"application": "foo", "profile": "development", "label": "master", "properties": { "property1": "value1", "property2": "value2" } });
EOF