package com.girlrising.Models.Aux.Map

  case class ExtendedData(name: String, displayName: String, value: String){
    def toKML = 
      <Data name="{this.name}">
        <displayName>{this.displayName}</displayName>
        <value>{this.value}</value>
      </Data>
  }