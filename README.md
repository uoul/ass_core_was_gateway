# WAS Gateway
The *WAS Gateway* fetches the current alerts from the **Alu2G** endpoint and fires messages
to the configured RabbitMQ broker if the polled message has changed. The messages are split into two groups
1. ActiveAlerts: This represents the list of all currently active alerts received from Alu2g
2. NewAlerts: New Alerts are those, which are new in the list of ActiveAlerts (only the new ones)
> **_NOTE:_** per **ASS** environment should run exactly one message source container
> that produces alertmessages on RabbitMQ exchange!

## Configuration
The entire configuration can be done via environment variables. The following settings
can be used:

| **Environment Variable**   | **Default Value** | **Explanation**                                                                      |
|----------------------------|-------------------|--------------------------------------------------------------------------------------|
| _WAS_GATEWAY_HTTP_PORT_    | 8080              | Quarkus HTTP Port                                                                    |
| _RABBITMQ_HOST_            | localhost         | RabbitMQ host                                                                        |
| _RABBITMQ_PORT_            | 5672              | Port of the RabbitMQ broker                                                          |
| _RABBITMQ_USER_            | guest             | RabbitMQ user                                                                        |
| _RABBITMQ_PW_              | guest             | RabbitMQ password                                                                    |
| _RABBITMQ_ACTIVE_EXCHANGE_ | ActiveAlerts      | If incoming alerts change, all alerts will be sent to this exchange                  |
| _RABBITMQ_NEW_EXCHANGE_    | NewAlerts         | If there are new alerts available, only the new ones will be pushed to this exchange |
| _WAS_HOST_                 | 192.168.130.100   | Alu2G host                                                                           |
| _WAS_PORT_                 | 47000             | Port of Alu2G endpoint                                                               |

> **_NOTE:_** Usually the default configuration should be set correct for your environment!