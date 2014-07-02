package com.girlrising.Models.Aux.Map

case class Style(id: String, icon: String){
		def toKML = <Style id="gr">
        <IconStyle>
          <Icon>
            <href>{ icon }
            </href>
          </Icon>
        </IconStyle>
      </Style>;
}