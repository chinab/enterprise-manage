package bondoffer

import (
	"reflect"
)

type Bond struct {
	LastVersion  string      `xml:"bondOfferMessage>lastVersion"`
	Version      string      `xml:"bondOfferMessage>version"`
	MethodAdd    []BondOffer `xml:"bondOfferMessage>methodAdd>bondOffer"`
	MethodUpdate []BondOffer `xml:"bondOfferMessage>methodUpdate>bondOffer"`
	MethodRefer  []BondOffer `xml:"bondOfferMessage>methodRefer>bondOffer"`
	MethodDeal   []BondOffer `xml:"bondOfferMessage>methodDeal>bondOffer"`
}

type BondOffer struct {
	Id               string `xml:"id"`
	AgentCode        string `xml:"agentCode"`
	BankAgentId      string `xml:"bankAgentId"`
	BankId           string `xml:"bankId"`
	BankName         string `xml:"bankName"`
	CompanyId        string `xml:"companyId"`
	CompanyName      string `xml:"companyName"`
	CreateTime       string `xml:"createTime"`
	DealStatus       string `xml:"dealStatus"`
	DealTime         string `xml:"dealTime"`
	Description      string `xml:"description"`
	FlagBad          string `xml:"flagBad"`
	FlagBargain      string `xml:"flagBargain"`
	FlagRelation     string `xml:"flagRelation"`
	FlagVip          string `xml:"flagVip"`
	GoodsCode        string `xml:"goodsCode"`
	GoodsId          string `xml:"goodsId"`
	GoodsLevel       string `xml:"goodsLevel"`
	GoodsShortName   string `xml:"goodsShortName"`
	GoodsTerm        string `xml:"goodsTerm"`
	GoodsType        string `xml:"goodsType"`
	Internally       string `xml:"internally"`
	OperatorId       string `xml:"operatorId"`
	Price            string `xml:"price"`
	PriceDescription string `xml:"priceDescription"`
	ProfitCode       string `xml:"profitCode"`
	QuoteType        string `xml:"quoteType"`
	RateFlag         string `xml:"rateFlag"`
	RateType         string `xml:"rateType"`
	Status           string `xml:"status"`
	Symbol           string `xml:"symbol"`
	Volume           string `xml:"volume"`
	Version          string
	LastVersion      string
	Method           string
}

func (offer BondOffer) ToMap() map[string]interface{} {
	myMap := make(map[string]interface{})
	offerType := reflect.TypeOf(offer)
	value := reflect.ValueOf(offer)
	for i := 0; i < offerType.NumField(); i++ {
		myMap[offerType.Field(i).Name] = value.Field(i).String()
	}
	return myMap
}
