/*
Copyright (c) 2009, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 3.0.0
build: 1549
*/
YUI.add("datasource-cache",function(B){var A=function(){A.superclass.constructor.apply(this,arguments);};B.mix(A,{NS:"cache",NAME:"dataSourceCache",ATTRS:{}});B.extend(A,B.Cache,{initializer:function(C){this.doBefore("_defRequestFn",this._beforeDefRequestFn);this.doBefore("_defResponseFn",this._beforeDefResponseFn);},_beforeDefRequestFn:function(D){var C=(this.retrieve(D.request))||null;if(C&&C.response){this.get("host").fire("response",B.mix({response:C.response},D));return new B.Do.Halt("DataSourceCache plugin halted _defRequestFn");}},_beforeDefResponseFn:function(C){if(C.response&&!C.response.cached){C.response.cached=true;this.add(C.request,C.response,(C.callback&&C.callback.argument));}}});B.namespace("Plugin").DataSourceCache=A;},"3.0.0",{requires:["datasource-local","cache"]});
