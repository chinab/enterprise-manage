/*     */ package com.idb.segmentation.dic;
/*     */ 
/*     */ import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import com.idb.segmentation.cfg.Configuration;
/*     */ 
/*     */ public class Dictionary
/*     */ {
/*     */   private static Dictionary singleton;
/*     */   private DictSegment _MainDict;
/*     */   private DictSegment _StopWordDict;
/*     */   private DictSegment _QuantifierDict;
/*     */   private Configuration cfg;
/*     */ 
/*     */   private Dictionary(Configuration cfg)
/*     */   {
/*  68 */     this.cfg = cfg;
/*  69 */     loadMainDict();
/*  70 */     loadStopWordDict();
/*  71 */     loadQuantifierDict();
/*     */   }
/*     */ 
/*     */   public static Dictionary initial(Configuration cfg)
/*     */   {
/*  83 */     if (singleton == null) {
/*  84 */       synchronized (Dictionary.class) {
/*  85 */         if (singleton == null) {
/*  86 */           singleton = new Dictionary(cfg);
/*  87 */           return singleton;
/*     */         }
/*     */       }
/*     */     }
/*  91 */     return singleton;
/*     */   }
/*     */ 
/*     */   public static Dictionary getSingleton()
/*     */   {
/*  99 */     if (singleton == null) {
/* 100 */       throw new IllegalStateException("词典尚未初始化，请先调用initial方法");
/*     */     }
/* 102 */     return singleton;
/*     */   }
/*     */ 
/*     */   public void addWords(Collection<String> words)
/*     */   {
/* 110 */     if (words != null)
/* 111 */       for (String word : words) {
/* 112 */         if (word == null)
/*     */           continue;
/* 114 */         singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void disableWords(Collection<String> words)
/*     */   {
/* 125 */     if (words != null)
/* 126 */       for (String word : words) {
/* 127 */         if (word == null)
/*     */           continue;
/* 129 */         singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
/*     */       }
/*     */   }
/*     */ 
/*     */   public Hit matchInMainDict(char[] charArray)
/*     */   {
/* 141 */     return singleton._MainDict.match(charArray);
/*     */   }
/*     */ 
/*     */   public Hit matchInMainDict(char[] charArray, int begin, int length)
/*     */   {
/* 152 */     return singleton._MainDict.match(charArray, begin, length);
/*     */   }
/*     */ 
/*     */   public Hit matchInQuantifierDict(char[] charArray, int begin, int length)
/*     */   {
/* 163 */     return singleton._QuantifierDict.match(charArray, begin, length);
/*     */   }
/*     */ 
/*     */   public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit)
/*     */   {
/* 175 */     DictSegment ds = matchedHit.getMatchedDictSegment();
/* 176 */     return ds.match(charArray, currentIndex, 1, matchedHit);
/*     */   }
/*     */ 
/*     */   public boolean isStopWord(char[] charArray, int begin, int length)
/*     */   {
/* 188 */     return singleton._StopWordDict.match(charArray, begin, length).isMatch();
/*     */   }
/*     */ 
/*     */   private void loadMainDict()
/*     */   {
/* 196 */     this._MainDict = new DictSegment(Character.valueOf('\000'));
/*     */ 
/* 198 */     InputStream is = getClass().getClassLoader().getResourceAsStream(this.cfg.getMainDictionary());
/* 199 */     if (is == null) {
/* 200 */       throw new RuntimeException("Main Dictionary not found!!!");
/*     */     }
/*     */     try
/*     */     {
/* 204 */       BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
/* 205 */       String theWord = null;
/*     */       do {
/* 207 */         theWord = br.readLine();
/* 208 */         if ((theWord != null) && (!"".equals(theWord.trim())))
/* 209 */           this._MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
/*     */       }
/* 211 */       while (theWord != null);
/*     */     }
/*     */     catch (IOException ioe) {
/* 214 */       System.err.println("Main Dictionary loading exception.");
/* 215 */       ioe.printStackTrace();
/*     */       try
/*     */       {
/* 219 */         if (is != null) {
/* 220 */           is.close();
/* 221 */           is = null;
/*     */         }
/*     */       } catch (IOException e) {
/* 224 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 219 */         if (is != null) {
/* 220 */           is.close();
/* 221 */           is = null;
/*     */         }
/*     */       } catch (IOException e) {
/* 224 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 228 */     loadExtDict();
/*     */   }
/*     */ 
/*     */   private void loadExtDict()
/*     */   {
/* 236 */     List<String> extDictFiles = this.cfg.getExtDictionarys();
/* 237 */     if (extDictFiles != null) {
/* 238 */       InputStream is = null;
/* 239 */       for (String extDictName : extDictFiles)
/*     */       {
/* 241 */         System.out.println("加载扩展词典：" + extDictName);
/* 242 */         is = getClass().getClassLoader().getResourceAsStream(extDictName);
/*     */ 
/* 244 */         if (is == null)
/*     */           continue;
/*     */         try
/*     */         {
/* 248 */           BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
/* 249 */           String theWord = null;
/*     */           do {
/* 251 */             theWord = br.readLine();
/* 252 */             if ((theWord == null) || ("".equals(theWord.trim()))) {
/*     */               continue;
/*     */             }
/* 255 */             this._MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
/*     */           }
/* 257 */           while (theWord != null);
/*     */         }
/*     */         catch (IOException ioe) {
/* 260 */           System.err.println("Extension Dictionary loading exception.");
/* 261 */           ioe.printStackTrace();
/*     */           try
/*     */           {
/* 265 */             if (is != null) {
/* 266 */               is.close();
/* 267 */               is = null;
/*     */             }
/*     */           } catch (IOException e) {
/* 270 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/*     */           try
/*     */           {
/* 265 */             if (is != null) {
/* 266 */               is.close();
/* 267 */               is = null;
/*     */             }
/*     */           } catch (IOException e) {
/* 270 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadStopWordDict()
/*     */   {
/* 282 */     this._StopWordDict = new DictSegment(Character.valueOf('\000'));
/*     */ 
/* 284 */     List<String> extStopWordDictFiles = this.cfg.getExtStopWordDictionarys();
/* 285 */     if (extStopWordDictFiles != null) {
/* 286 */       InputStream is = null;
/* 287 */       for (String extStopWordDictName : extStopWordDictFiles) {
/* 288 */         System.out.println("加载扩展停止词典：" + extStopWordDictName);
/*     */ 
/* 290 */         is = getClass().getClassLoader().getResourceAsStream(extStopWordDictName);
/*     */ 
/* 292 */         if (is == null)
/*     */           continue;
/*     */         try
/*     */         {
/* 296 */           BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
/* 297 */           String theWord = null;
/*     */           do {
/* 299 */             theWord = br.readLine();
/* 300 */             if ((theWord == null) || ("".equals(theWord.trim()))) {
/*     */               continue;
/*     */             }
/* 303 */             this._StopWordDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
/*     */           }
/* 305 */           while (theWord != null);
/*     */         }
/*     */         catch (IOException ioe) {
/* 308 */           System.err.println("Extension Stop word Dictionary loading exception.");
/* 309 */           ioe.printStackTrace();
/*     */           try
/*     */           {
/* 313 */             if (is != null) {
/* 314 */               is.close();
/* 315 */               is = null;
/*     */             }
/*     */           } catch (IOException e) {
/* 318 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/*     */           try
/*     */           {
/* 313 */             if (is != null) {
/* 314 */               is.close();
/* 315 */               is = null;
/*     */             }
/*     */           } catch (IOException e) {
/* 318 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadQuantifierDict()
/*     */   {
/* 330 */     this._QuantifierDict = new DictSegment(Character.valueOf('\000'));
/*     */ 
/* 332 */     InputStream is = getClass().getClassLoader().getResourceAsStream(this.cfg.getQuantifierDicionary());
/* 333 */     if (is == null)
/* 334 */       throw new RuntimeException("Quantifier Dictionary not found!!!");
/*     */     try
/*     */     {
/* 337 */       BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
/* 338 */       String theWord = null;
/*     */       do {
/* 340 */         theWord = br.readLine();
/* 341 */         if ((theWord != null) && (!"".equals(theWord.trim())))
/* 342 */           this._QuantifierDict.fillSegment(theWord.trim().toCharArray());
/*     */       }
/* 344 */       while (theWord != null);
/*     */     }
/*     */     catch (IOException ioe) {
/* 347 */       System.err.println("Quantifier Dictionary loading exception.");
/* 348 */       ioe.printStackTrace();
/*     */       try
/*     */       {
/* 352 */         if (is != null) {
/* 353 */           is.close();
/* 354 */           is = null;
/*     */         }
/*     */       } catch (IOException e) {
/* 357 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       try
/*     */       {
/* 352 */         if (is != null) {
/* 353 */           is.close();
/* 354 */           is = null;
/*     */         }
/*     */       } catch (IOException e) {
/* 357 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\TDDOWNLOAD\IKAnalyzer2012_u4\
 * Qualified Name:     org.wltea.analyzer.dic.Dictionary
 * JD-Core Version:    0.6.0
 */