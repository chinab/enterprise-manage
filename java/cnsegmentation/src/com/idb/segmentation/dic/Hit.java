/*     */ package com.idb.segmentation.dic;
/*     */ 
/*     */ public class Hit
/*     */ {
/*     */   private static final int UNMATCH = 0;
/*     */   private static final int MATCH = 1;
/*     */   private static final int PREFIX = 16;
/*  41 */   private int hitState = UNMATCH;
/*     */   private DictSegment matchedDictSegment;
/*     */   private int begin;
/*     */   private int end;
/*     */ 
/*     */   public boolean isMatch()
/*     */   {
/*  59 */     return (this.hitState & 0x1) > 0;
/*     */   }
/*     */ 
/*     */   public void setMatch()
/*     */   {
/*  65 */     this.hitState |= MATCH;
/*     */   }
/*     */ 
/*     */   public boolean isPrefix()
/*     */   {
/*  72 */     return (this.hitState & 0x10) > 0;
/*     */   }
/*     */ 
/*     */   public void setPrefix()
/*     */   {
/*  78 */     this.hitState |= PREFIX;
/*     */   }
/*     */ 
/*     */   public boolean isUnmatch()
/*     */   {
/*  84 */     return this.hitState == UNMATCH;
/*     */   }
/*     */ 
/*     */   public void setUnmatch()
/*     */   {
/*  90 */     this.hitState = UNMATCH;
/*     */   }
/*     */ 
/*     */   public DictSegment getMatchedDictSegment() {
/*  94 */     return this.matchedDictSegment;
/*     */   }
/*     */ 
/*     */   public void setMatchedDictSegment(DictSegment matchedDictSegment) {
/*  98 */     this.matchedDictSegment = matchedDictSegment;
/*     */   }
/*     */ 
/*     */   public int getBegin() {
/* 102 */     return this.begin;
/*     */   }
/*     */ 
/*     */   public void setBegin(int begin) {
/* 106 */     this.begin = begin;
/*     */   }
/*     */ 
/*     */   public int getEnd() {
/* 110 */     return this.end;
/*     */   }
/*     */ 
/*     */   public void setEnd(int end) {
/* 114 */     this.end = end;
/*     */   }
/*     */ }

/* Location:           D:\TDDOWNLOAD\IKAnalyzer2012_u4\
 * Qualified Name:     org.wltea.analyzer.dic.Hit
 * JD-Core Version:    0.6.0
 */