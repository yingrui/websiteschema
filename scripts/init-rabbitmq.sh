#! /bin/bash

# Start rabbitmq server
rabbitmq-server -detached
rabbitmq-plugins enable rabbitmq_management

# Add user
rabbitmq add_user websiteschema websiteschema
rabbitmqctl set_user_tags websiteschema administrator
rabbitmq list_users

# Set permissions
rabbitmqctl set_permissions -p / websiteschema '.*' '.*' '.*'
rabbitmqctl list_user_permissions websiteschema
