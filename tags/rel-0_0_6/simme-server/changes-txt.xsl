<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
  Text output guides:

    &#x9;   = tab character
    &#xA;   = newline character
    &#xA0;  = non-breaking space character

-->
  <!-- This stylesheet produces text in UTF-8 -->
  <xsl:output method="text" encoding="utf-8"/>

  <xsl:template match="/">
    <xsl:value-of select="changelog/@title"/>
    <xsl:text>&#xA;</xsl:text>

    <xsl:for-each select="changelog/release">
      <xsl:sort select="@date" order="descending"/>
      <xsl:text>&#xA;&#xA;&#x9;</xsl:text>
      <xsl:text>[</xsl:text>
      <xsl:value-of select="@date"/>
      <xsl:text>]</xsl:text>
      <xsl:text> Release </xsl:text>
      <xsl:value-of select="@name"/>

      <xsl:apply-templates select="changes"/>
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="changes">
    <xsl:text>&#xA;</xsl:text>
    <xsl:for-each select="change">
      <xsl:text>&#xA;&#x9;&#x9;* </xsl:text>
      <xsl:apply-templates select="description"/>
      <xsl:apply-templates select="bugs"/>
      <xsl:apply-templates select="changer"/>
      <xsl:apply-templates select="../changer"/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="bugs">
    <xsl:text> [</xsl:text>
    <xsl:for-each select="bug">
      <xsl:sort select="@id"/>
      <xsl:text> #</xsl:text>
      <xsl:value-of select="@id"/>
    </xsl:for-each>
    <xsl:text> ]</xsl:text>
  </xsl:template>

  <xsl:template match="changer">
    <xsl:text> (</xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>)</xsl:text>
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
