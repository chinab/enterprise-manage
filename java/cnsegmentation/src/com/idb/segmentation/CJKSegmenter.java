/*     */ package com.idb.segmentation;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;

import com.idb.segmentation.dic.Dictionary;
import com.idb.segmentation.dic.Hit;
/*     */ 
/*     */ class CJKSegmenter
/*     */   implements ISegmenter
/*     */ {
/*     */   static final String SEGMENTER_NAME = "CJK_SEGMENTER";
/*     */   private List<Hit> tmpHits;
/*     */ 
/*     */   CJKSegmenter()
/*     */   {
/*  47 */     this.tmpHits = new LinkedList();
/*     */   }
/*     */ 
/*     */   public void analyze(AnalyzeContext context)
/*     */   {
/*  54 */     if (context.getCurrentCharType() != 0)
/*     */     {
/*  57 */       if (!this.tmpHits.isEmpty())
/*     */       {
/*  59 */         Hit[] tmpArray = (Hit[])this.tmpHits.toArray(new Hit[this.tmpHits.size()]);
/*  60 */         for (Hit hit : tmpArray) {
/*  61 */           hit = Dictionary.getSingleton().matchWithHit(context.getSegmentBuff(), context.getCursor(), hit);
/*  62 */           if (hit.isMatch())
/*     */           {
/*  64 */             Lexeme newLexeme = new Lexeme(context.getBufferOffset(), hit.getBegin(), context.getCursor() - hit.getBegin() + 1, 4);
/*  65 */             context.addLexeme(newLexeme);
/*     */ 
/*  67 */             if (!hit.isPrefix())
/*  68 */               this.tmpHits.remove(hit);
/*     */           }
/*     */           else {
/*  71 */             if (!hit.isUnmatch())
/*     */               continue;
/*  73 */             this.tmpHits.remove(hit);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  80 */       Hit singleCharHit = Dictionary.getSingleton().matchInMainDict(context.getSegmentBuff(), context.getCursor(), 1);
/*  81 */       if (singleCharHit.isMatch())
/*     */       {
/*  83 */         Lexeme newLexeme = new Lexeme(context.getBufferOffset(), context.getCursor(), 1, 4);
/*  84 */         context.addLexeme(newLexeme);
/*     */ 
/*  87 */         if (singleCharHit.isPrefix())
/*     */         {
/*  89 */           this.tmpHits.add(singleCharHit);
/*     */         }
/*  91 */       } else if (singleCharHit.isPrefix())
/*     */       {
/*  93 */         this.tmpHits.add(singleCharHit);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 100 */       this.tmpHits.clear();
/*     */     }
/*     */ 
/* 104 */     if (context.isBufferConsumed())
/*     */     {
/* 106 */       this.tmpHits.clear();
/*     */     }
/*     */ 
/* 110 */     if (this.tmpHits.size() == 0) {
/* 111 */       context.unlockBuffer(SEGMENTER_NAME);
/*     */     }
/*     */     else
/* 114 */       context.lockBuffer(SEGMENTER_NAME);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 123 */     this.tmpHits.clear();
/*     */   }
/*     */ }

/* Location:           D:\TDDOWNLOAD\IKAnalyzer2012_u4\
 * Qualified Name:     org.wltea.analyzer.core.CJKSegmenter
 * JD-Core Version:    0.6.0
 */