#  Numerical type percent
The query for this check calculates the percentage of numerical values from column.

Successfully classified records are assigned value of 1, and any other values, 0.
Those values are then summed (so effectively we perform count of valid values), divided by the number of records,
and multiplicated by a 100.0 so that the results is in percent.
___
## Jinja Template
=== "BigQuery"
    ```
    --8<-- "home/sensors/column/validity/numerical_type_percent/bigquery.sql.jinja2"
    ```

=== "Snowflake"
    ```
    --8<-- "home/sensors/column/validity/numerical_type_percent/bigquery.sql.jinja2"
    ```