package osm

import (
	"fmt"
	"reflect"
)

func resultValue(o *Osm, sm *sqlMapper, sqlParams []interface{}, containers []interface{}) (int64, error) {
	values := make([]reflect.Value, len(containers))

	for i, container := range containers {
		pointValue := reflect.ValueOf(container)
		if pointValue.Kind() != reflect.Ptr {
			panic(fmt.Errorf("Query()() all args must be use ptr"))
		}

		value := reflect.Indirect(pointValue)
		values[i] = value
	}

	rows, err := o.db.Query(sm.sql, sqlParams...)
	if err != nil {
		return 0, err
	}

	defer rows.Close()

	var rowsCount int64

	if rows.Next() {

		columns, err := rows.Columns()
		if err != nil {
			return 0, err
		}

		if len(containers) != len(columns) {
			return 0, fmt.Errorf("len(containers) != len(columns)")
		}

		refs := make([]interface{}, len(columns))
		for i, _ := range columns {
			var ref interface{}
			refs[i] = &ref
		}

		if err := rows.Scan(refs...); err != nil {
			return 0, err
		}

		for i, v := range refs {
			vv := reflect.ValueOf(v).Elem().Interface()
			setDataToValue(values[i], vv)
		}

		rowsCount++
	}

	return rowsCount, nil
}
