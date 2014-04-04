package osm

import (
	"database/sql"
	"fmt"
	"reflect"
)

type Osm struct {
	db            *sql.DB
	sqlMappersMap map[string]*sqlMapper
}

func NewOsm(driverName, dataSource string, xmlPaths []string, params ...int) (osm *Osm, err error) {
	osm = new(Osm)
	db, err := sql.Open(driverName, dataSource)
	if err != nil {
		if db != nil {
			db.Close()
		}
		err = fmt.Errorf("create osm %s", err.Error())
		return
	}

	osm.db = db
	osm.sqlMappersMap = make(map[string]*sqlMapper)

	for i, v := range params {
		switch i {
		case 0:
			osm.db.SetMaxIdleConns(v)
		case 1:
			osm.db.SetMaxOpenConns(v)
		}
	}

	for _, xmlPath := range xmlPaths {
		sqlMappers, err := readMappers(xmlPath)
		if err == nil {
			for _, sm := range sqlMappers {
				osm.sqlMappersMap[sm.id] = sm
			}
		} else {
			err = fmt.Errorf("read sqlMappers %s", err.Error())
		}
	}

	return
}

func (o *Osm) Query(id string, param interface{}) (err error) {

	sm, ok := o.sqlMappersMap[id]
	if !ok {
		return fmt.Errorf("Query \"%s\" error ,id not fond ", id)
	}

	var params []interface{}

	v := reflect.ValueOf(param)

	kind := v.Kind()
	switch {
	case kind == reflect.Array || kind == reflect.Slice:
		params, ok = param.([]interface{})
		if !ok {
			err = fmt.Errorf("array type not []interface{} of %s", param)
			return
		}
	case kind == reflect.Map:
		for _, paramName := range sm.paramNames {
			p, ok := param.(map[string]interface{})
			if ok {
				params = append(params, p[paramName])
			} else {
				err = fmt.Errorf("array type not map[string]interface{} of %s", param)
				return
			}
		}
	case kind == reflect.Struct:
		for _, paramName := range sm.paramNames {
			params = append(params, v.FieldByName(paramName))
		}
	case kind == reflect.Bool ||
		kind == reflect.Int ||
		kind == reflect.Int8 ||
		kind == reflect.Int16 ||
		kind == reflect.Int32 ||
		kind == reflect.Int64 ||
		kind == reflect.Uint ||
		kind == reflect.Uint8 ||
		kind == reflect.Uint16 ||
		kind == reflect.Uint32 ||
		kind == reflect.Uint64 ||
		kind == reflect.Uintptr ||
		kind == reflect.Float32 ||
		kind == reflect.Float64 ||
		kind == reflect.Complex64 ||
		kind == reflect.Complex128 ||
		kind == reflect.String:
		params = append(params, param)
	default:
	}

	stmt, err := o.db.Prepare(sm.sql)
	if err != nil {
		return
	}
	fmt.Println(sm.sql)
	rows, err := stmt.Query(params...)
	if err != nil {
		return
	}

	for rows.Next() {
		var uid int
		var username string
		err = rows.Scan(&uid, &username)

		fmt.Println(uid)
		fmt.Println(username)
	}
	return
}
