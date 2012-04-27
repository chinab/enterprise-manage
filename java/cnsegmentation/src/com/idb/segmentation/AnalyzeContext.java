/*     */ package com.idb.segmentation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;

import com.idb.segmentation.cfg.Configuration;
import com.idb.segmentation.dic.Dictionary;
/*     */ 
/*     */ class AnalyzeContext
/*     */ {
/*     */   private static final int BUFF_SIZE = 3072;
/*     */   private static final int BUFF_EXHAUST_CRITICAL = 48;
/*     */   private char[] segmentBuff;
/*     */   private int[] charTypes;
/*     */   private int buffOffset;
/*     */   private int cursor;
/*     */   private int available;
/*     */   private Set<String> buffLocker;
/*     */   private QuickSortSet orgLexemes;
/*     */   private Map<Integer, LexemePath> pathMap;
/*     */   private LinkedList<Lexeme> results;
/*     */   private Configuration cfg;
/*     */ 
/*     */   public AnalyzeContext(Configuration cfg)
/*     */   {
/*  81 */     this.cfg = cfg;
/*  82 */     this.segmentBuff = new char[BUFF_SIZE];
/*  83 */     this.charTypes = new int[BUFF_SIZE];
/*  84 */     this.buffLocker = new HashSet();
/*  85 */     this.orgLexemes = new QuickSortSet();
/*  86 */     this.pathMap = new HashMap();
/*  87 */     this.results = new LinkedList();
/*     */   }
/*     */ 
/*     */   int getCursor() {
/*  91 */     return this.cursor;
/*     */   }
/*     */ 
/*     */   char[] getSegmentBuff()
/*     */   {
/*  99 */     return this.segmentBuff;
/*     */   }
/*     */ 
/*     */   char getCurrentChar() {
/* 103 */     return this.segmentBuff[this.cursor];
/*     */   }
/*     */ 
/*     */   int getCurrentCharType() {
/* 107 */     return this.charTypes[this.cursor];
/*     */   }
/*     */ 
/*     */   int getBufferOffset() {
/* 111 */     return this.buffOffset;
/*     */   }
/*     */ 
/*     */   int fillBuffer(Reader reader)
/*     */     throws IOException
/*     */   {
/* 121 */     int readCount = 0;
/* 122 */     if (this.buffOffset == 0)
/*     */     {
/* 124 */       readCount = reader.read(this.segmentBuff);
/*     */     } else {
/* 126 */       int offset = this.available - this.cursor;
/* 127 */       if (offset > 0)
/*     */       {
/* 129 */         System.arraycopy(this.segmentBuff, this.cursor, this.segmentBuff, 0, offset);
/* 130 */         readCount = offset;
/*     */       }
/*     */ 
/* 133 */       readCount += reader.read(this.segmentBuff, offset, BUFF_SIZE - offset);
/*     */     }
/*     */ 
/* 136 */     this.available = readCount;
/*     */ 
/* 138 */     this.cursor = 0;
/* 139 */     return readCount;
/*     */   }
/*     */ 
/*     */   void initCursor()
/*     */   {
/* 146 */     this.cursor = 0;
/* 147 */     this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
/* 148 */     this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
/*     */   }
/*     */ 
/*     */   boolean moveCursor()
/*     */   {
/* 157 */     if (this.cursor < this.available - 1) {
/* 158 */       this.cursor += 1;
/* 159 */       this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
/* 160 */       this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
/* 161 */       return true;
/*     */     }
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   void lockBuffer(String segmenterName)
/*     */   {
/* 173 */     this.buffLocker.add(segmenterName);
/*     */   }
/*     */ 
/*     */   void unlockBuffer(String segmenterName)
/*     */   {
/* 181 */     this.buffLocker.remove(segmenterName);
/*     */   }
/*     */ 
/*     */   boolean isBufferLocked()
/*     */   {
/* 190 */     return this.buffLocker.size() > 0;
/*     */   }
/*     */ 
/*     */   boolean isBufferConsumed()
/*     */   {
/* 199 */     return this.cursor == this.available - 1;
/*     */   }
/*     */ 
/*     */   boolean needRefillBuffer()
/*     */   {
/* 216 */     return (this.available == BUFF_SIZE) && 
/* 214 */       (this.cursor < this.available - 1) && 
/* 215 */       (this.cursor > this.available - BUFF_EXHAUST_CRITICAL) && 
/* 216 */       (!isBufferLocked());
/*     */   }
/*     */ 
/*     */   void markBufferOffset()
/*     */   {
/* 223 */     this.buffOffset += this.cursor;
/*     */   }
/*     */ 
/*     */   void addLexeme(Lexeme lexeme)
/*     */   {
/* 231 */     this.orgLexemes.addLexeme(lexeme);
/*     */   }
/*     */ 
/*     */   void addLexemePath(LexemePath path)
/*     */   {
/* 240 */     if (path != null)
/* 241 */       this.pathMap.put(Integer.valueOf(path.getPathBegin()), path);
/*     */   }
/*     */ 
/*     */   QuickSortSet getOrgLexemes()
/*     */   {
/* 251 */     return this.orgLexemes;
/*     */   }
/*     */ 
/*     */   void processUnkownCJKChar()
/*     */   {
/* 258 */     int index = 0;
/* 259 */     while (index < this.available)
/*     */     {
/* 261 */       if (this.charTypes[index] == 0) {
/* 262 */         index++;
/*     */       }
/*     */       else
/*     */       {
/* 266 */         LexemePath path = (LexemePath)this.pathMap.get(Integer.valueOf(index));
/* 267 */         if (path != null)
/*     */         {
/* 269 */           Lexeme l = path.pollFirst();
/* 270 */           while (l != null) {
/* 271 */             this.results.add(l);
/*     */ 
/* 273 */             index = l.getBegin() + l.getLength();
/* 274 */             l = path.pollFirst();
/* 275 */             if (l == null)
/*     */               continue;
/* 277 */             for (; index < l.getBegin(); index++) {
/* 278 */               outputSingleCJK(index);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 284 */           outputSingleCJK(index);
/* 285 */           index++;
/*     */         }
/*     */       }
/*     */     }
/* 289 */     this.pathMap.clear();
/*     */   }
/*     */ 
/*     */   private void outputSingleCJK(int index)
/*     */   {
/* 297 */     if (4 == this.charTypes[index]) {
/* 298 */       Lexeme singleCharLexeme = new Lexeme(this.buffOffset, index, 1, 4);
/* 299 */       this.results.add(singleCharLexeme);
/* 300 */     } else if (8 == this.charTypes[index]) {
/* 301 */       Lexeme singleCharLexeme = new Lexeme(this.buffOffset, index, 1, 8);
/* 302 */       this.results.add(singleCharLexeme);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean hasNextResult()
/*     */   {
/* 311 */     return !this.results.isEmpty();
/*     */   }
/*     */ 
/*     */   Lexeme getNextLexeme()
/*     */   {
/* 322 */     Lexeme result = (Lexeme)this.results.pollFirst();
/* 323 */     while (result != null)
/*     */     {
/* 325 */       compound(result);
/* 326 */       if (Dictionary.getSingleton().isStopWord(this.segmentBuff, result.getBegin(), result.getLength()))
/*     */       {
/* 328 */         result = (Lexeme)this.results.pollFirst();
/*     */       }
/*     */       else {
/* 331 */         result.setLexemeText(String.valueOf(this.segmentBuff, result.getBegin(), result.getLength()));
/* 332 */         break;
/*     */       }
/*     */     }
/* 335 */     return result;
/*     */   }
/*     */ 
/*     */   void reset()
/*     */   {
/* 342 */     this.buffLocker.clear();
/* 343 */     this.orgLexemes = new QuickSortSet();
/* 344 */     this.available = 0;
/* 345 */     this.buffOffset = 0;
/* 346 */     this.charTypes = new int[BUFF_SIZE];
/* 347 */     this.cursor = 0;
/* 348 */     this.results.clear();
/* 349 */     this.segmentBuff = new char[BUFF_SIZE];
/*     */   }
/*     */ 
/*     */   private void compound(Lexeme result)
/*     */   {
/* 356 */     if (!this.cfg.useSmart()) {
/* 357 */       return;
/*     */     }
/*     */ 
/* 360 */     if (!this.results.isEmpty())
/*     */     {
/* 362 */       if (2 == result.getLexemeType()) {
/* 363 */         Lexeme nextLexeme = (Lexeme)this.results.peekFirst();
/* 364 */         boolean appendOk = false;
/* 365 */         if (16 == nextLexeme.getLexemeType())
/*     */         {
/* 367 */           appendOk = result.append(nextLexeme, 16);
/* 368 */         } else if (32 == nextLexeme.getLexemeType())
/*     */         {
/* 370 */           appendOk = result.append(nextLexeme, BUFF_EXHAUST_CRITICAL);
/*     */         }
/* 372 */         if (appendOk)
/*     */         {
/* 374 */           this.results.pollFirst();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 379 */       if ((16 == result.getLexemeType()) && (!this.results.isEmpty())) {
/* 380 */         Lexeme nextLexeme = (Lexeme)this.results.peekFirst();
/* 381 */         boolean appendOk = false;
/* 382 */         if (32 == nextLexeme.getLexemeType())
/*     */         {
/* 384 */           appendOk = result.append(nextLexeme, BUFF_EXHAUST_CRITICAL);
/*     */         }
/* 386 */         if (appendOk)
/*     */         {
/* 388 */           this.results.pollFirst();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\TDDOWNLOAD\IKAnalyzer2012_u4\
 * Qualified Name:     org.wltea.analyzer.core.AnalyzeContext
 * JD-Core Version:    0.6.0
 */