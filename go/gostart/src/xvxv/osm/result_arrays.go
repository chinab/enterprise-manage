package osm

import (
	"fmt"
	"reflect"
)

func resultArrays(o *Osm, sm *sqlMapper, sqlParams []interface{}, container interface{}) (int64, error) {

	pointValue := reflect.ValueOf(container)
	if pointValue.Kind() != reflect.Ptr {
		panic(fmt.Errorf("Query()() all args must be use ptr"))
	}

	value := reflect.Indirect(pointValue)

	valueNew := make([][]Data, 0)

	rows, err := o.db.Query(sm.sql, sqlParams...)
	if err != nil {
		return 0, err
	}

	defer rows.Close()

	var rowsCount int64

	for rows.Next() {
		columns, err := rows.Columns()
		if err != nil {
			return 0, err
		}

		refs := make([]interface{}, len(columns))
		for i, _ := range columns {
			var ref interface{}
			refs[i] = &ref
		}

		if err := rows.Scan(refs...); err != nil {
			return 0, err
		}

		vvs := make([]Data, len(columns))
		for i, v := range refs {
			vv := reflect.ValueOf(v).Elem().Interface()
			vvs[i].d = vv
		}

		valueNew = append(valueNew, vvs)

		rowsCount++
	}

	value.Set(reflect.ValueOf(valueNew))

	return rowsCount, nil
}
