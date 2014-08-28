<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
            <title>Listing Country Items</title>
            <link rel="stylesheet" type="text/css" href="/commonInfrastructure-war/jsfcrud.css" />
        </head>
        <body>
            <h:panelGroup id="messagePanel" layout="block">
                <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
            </h:panelGroup>
            <h1>Listing Country Items</h1>
            <h:form styleClass="jsfcrud_list_form">
                <h:outputText escape="false" value="(No Country Items Found)<br />" rendered="#{country.pagingInfo.itemCount == 0}" />
                <h:panelGroup rendered="#{country.pagingInfo.itemCount > 0}">
                    <h:outputText value="Item #{country.pagingInfo.firstItem + 1}..#{country.pagingInfo.lastItem} of #{country.pagingInfo.itemCount}"/>&nbsp;
                    <h:commandLink action="#{country.prev}" value="Previous #{country.pagingInfo.batchSize}" rendered="#{country.pagingInfo.firstItem >= country.pagingInfo.batchSize}"/>&nbsp;
                    <h:commandLink action="#{country.next}" value="Next #{country.pagingInfo.batchSize}" rendered="#{country.pagingInfo.lastItem + country.pagingInfo.batchSize <= country.pagingInfo.itemCount}"/>&nbsp;
                    <h:commandLink action="#{country.next}" value="Remaining #{country.pagingInfo.itemCount - country.pagingInfo.lastItem}"
                                   rendered="#{country.pagingInfo.lastItem < country.pagingInfo.itemCount && country.pagingInfo.lastItem + country.pagingInfo.batchSize > country.pagingInfo.itemCount}"/>
                    <h:dataTable value="#{country.countryItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Id"/>
                            </f:facet>
                            <h:outputText value="#{item.id}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Code"/>
                            </f:facet>
                            <h:outputText value="#{item.code}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Name"/>
                            </f:facet>
                            <h:outputText value="#{item.name}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Stores"/>
                            </f:facet>
                            <h:outputText value="#{item.stores}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>
                            <h:commandLink value="Show" action="#{country.detailSetup}">
                                <f:param name="jsfcrud.currentCountry" value="#{jsfcrud_class['jsf.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][country.converter].jsfcrud_invoke}"/>
                            </h:commandLink>
                            <h:outputText value=" "/>
                            <h:commandLink value="Edit" action="#{country.editSetup}">
                                <f:param name="jsfcrud.currentCountry" value="#{jsfcrud_class['jsf.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][country.converter].jsfcrud_invoke}"/>
                            </h:commandLink>
                            <h:outputText value=" "/>
                            <h:commandLink value="Destroy" action="#{country.remove}">
                                <f:param name="jsfcrud.currentCountry" value="#{jsfcrud_class['jsf.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][country.converter].jsfcrud_invoke}"/>
                            </h:commandLink>
                        </h:column>

                    </h:dataTable>
                </h:panelGroup>
                <br />
                <h:commandLink action="#{country.createSetup}" value="New Country"/>
                <br />
                <br />
                <h:commandLink value="Index" action="welcome" immediate="true" />


            </h:form>
        </body>
    </html>
</f:view>
