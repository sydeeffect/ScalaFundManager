package org.syde.fundmanagerrack.types

import java.util.Calendar
import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global

// Generates Unique Customer ID
object CustomerID {
  // 1 Customer will be HARCODED at the start of the server.
  var id = 1
  // generate -- Generates uniqe ID, Synchromnized to tackle concurency
  def generate():Int = this.synchronized {
    id = id + 1
    return id
  }
}

// Class Customer -- Represents Customer
class Customer( var name:String, var dob:java.util.Date, var id:Int = CustomerID.generate(), val createdAt:java.util.Date = Calendar.getInstance.getTime()) {
  // getName -- Returns name of Customer
  def getName = name
  // getID -- Returns ID
  def getID = id
}

// Object CustomerDB -- Represents Storage for Customer Database
object CustomerDB {
  private var Store:Map[Int,Customer] = Map()

  // Creates Customer
  def CreateCustomer(name:String, dob:java.util.Date):Customer = {
    val c = new Customer(name, dob)
    Store += (c.getID -> c)
    return c
  }

  // Fetches Customer
  def FetchCustomer(id:Int):Option[Customer] = {
    return Store.get(id)
  }
}

// Class FundDB -- Represents Storage for a fund
object FundDB {
  private var Store:Map[String, Fund] = Map()
  // Initialize with hardcoded value
  def Init():Unit = {
    val a = new Fund("FOUSA08P20", "S&P GSCI Gold", 10)
    val b = new Fund("IE00BF20L762", "Dimensional World Equity Fund", 10)
    val c = new Fund("XIUSA04G92", "S&P 500", 10)
    Store += (a.getISIN -> a)
    Store += (b.getISIN -> b)
    Store += (c.getISIN -> c)
  }

  // Initialize
  Init()

  // FetchFund -- Return the fund details
  def FetchFund(isin:String):Option[Fund] = {
    return Store.get(isin)
  }

  // Updates Fund from Market Price
  def UpdateFundDB(MarketPrice:List[Fund]):Unit = {
    // TODO: Looks imperative. Find Pure Functional alternative
    for(fund <- MarketPrice ) Store += (fund.getISIN -> fund)
  }
}

// Class Fund -- Represents a fund
class Fund(val ISIN:String, val Name:String, var CurrentPrice:Float) {
  // UpdatePrice -- Update fund price to new a Price
  def UpdatePrice(newPrice:Float):Unit = {
    CurrentPrice = newPrice
  }
  // GetsPrice -- Get Current Price of Fund
  def GetPrice = CurrentPrice

  // GetStockWorth -- Converts Amount(SGD) to Equivalent Stocks
  def GetStockWorth(amt:Float): Float = {
    return CurrentPrice/amt
  }

  // GetAmountWorth -- Converts Stock to Equivalient Price
  def GetAmountWorth(stock:Float): Float = {
    return CurrentPrice*stock
  }

  // getISIS -- Returns ISIS
  def getISIN = ISIN
}


// Class MarketPriceDB -- Reprepsents Storage for Market Price
object MarketPriceDB {
  // TODO: Key is string of date. Find Alternative that binds to native date type
  private var Store:Map[String, List[Fund]] = Map()

  // Init -- Initiliaze the DB with Hardcoded Values
  def Init():Unit = {
    
    // TODO: Recommend to move to a time series Storage
    val a = new Fund("FOUSA08P20", "S&P GSCI Gold", 10)::new Fund("IE00BF20L762", "Dimensional World Equity Fund", 24)::new Fund("XIUSA04G92", "S&P 500", 65)::Nil
    val b = new Fund("FOUSA08P20", "S&P GSCI Gold", 20)::new Fund("IE00BF20L762", "Dimensional World Equity Fund", 22)::new Fund("XIUSA04G92", "S&P 500", 60)::Nil
    val c = new Fund("FOUSA08P20", "S&P GSCI Gold", 30)::new Fund("IE00BF20L762", "Dimensional World Equity Fund", 24)::new Fund("XIUSA04G92", "S&P 500", 12)::Nil
    val d = new Fund("FOUSA08P20", "S&P GSCI Gold", 40)::new Fund("IE00BF20L762", "Dimensional World Equity Fund", 20)::new Fund("XIUSA04G92", "S&P 500", 45)::Nil
    Store += ("2019-12-11" -> a)
    Store += ("2019-12-12" -> b)
    Store += ("2019-12-13" -> c)
    Store += ("2019-12-14" -> d)
    Store += ("2019-12-15" -> a)
    Store += ("2019-12-16" -> b)
    Store += ("2019-12-17" -> c)
    Store += ("2019-12-18" -> d)
    Store += ("2019-12-19" -> a)
    Store += ("2019-12-20" -> b)
    Store += ("2019-12-21" -> c)
    Store += ("2019-12-22" -> d)
  }

  // TODO: 
  // GetMarketPrice -- Returns Price for all Funds  for a given date
  def GetMarketPrice(date:String):List[Fund] = {
    val a = List[Fund]()
    // STUPID SOLUTION TODO: Figure out sane way to manage NONE condition
    return Store.get(date).getOrElse(a)
  }
}

// Class AccountDB -- Represents Storage for Account
object AccountDB {
  private var Store:Map[Int,Account] = Map(1 -> new Account)

  // FetchAccount -- Returns Account for a Customer
  def FetchAccount(id:Int):Option[Account] = {
    return Store.get(id)
  }

  // Create Account -- Creates an Empty Account for Customer
  def CreateAccount(id:Int) = {
    //TODO: Check if account already Exists
    Store += (id -> new Account)
  }
}

// Class Asset --  Stores Position of a Customer against Various Stocks 
 class Asset(var Position:String, var Stock:Float, var FundID:String) {
   def getStock = Stock
   def getISIN = FundID
   def updateStock(newStock:Float) = {
     Stock = newStock
   }
 }

// Class Account -- Encapsulates Amount and Assest for a customer
class Account() {
   private var CashBalance:Float = 0
   private var Assets:List[Asset] = List()  

   // Buy --  Buys a fund
   def Buy(fund:String, stock:Float):Unit = {
     Assets.find(asset => asset.FundID == fund) match {
       case Some(i) => {
         i.updateStock(i.getStock + stock)
       }
       case None =>  {
         val ast = new Asset("Buy", stock, fund)
         Assets = ast::Assets 
       }
     }
   }

   // Sell -- Sells a fund
   def Sell(fund:String, stock:Float):Boolean = {
     Assets.find(asset => asset.FundID == fund) match {
       case Some(i) => {
         i.updateStock(i.getStock - stock)
         return true
       }
       case None => return false
     } 
   }

   // Short -- Shorts a fund
   def Short(fund:String, stock:Float):Unit = {
     val ast = new Asset("Short", stock, fund)
     Assets = ast::Assets 
   }
    
   // GetBalance -- Returns Cash Balance
   def GetBalance = CashBalance


   // HasStock -- Checks if Stocks are sufficiently available
   def HasStock(FundID:String, Stock:Float):Boolean = this.synchronized {
     Assets.find(asset => asset.getISIN == FundID) match {
       case Some(i) => return (i.getStock < Stock)
       case None => return false
     } 
   }

   // CreditAccount -- Adds Money to CashBalance. Synchronized for tackling Concurrency
   def CreditAccount(amount:Float):Float = this.synchronized {
     CashBalance = CashBalance + amount 
     return CashBalance
   }

   // HasBalance -- Checks if Sufficient balance exist
   def HasBalance(amount:Float):Boolean = this.synchronized {
     return (CashBalance > amount)
   }
 
   // DebitAccount -- Debits money from cashBalance
   def DebitAccount(amount:Float):Unit = this.synchronized {
     CashBalance = CashBalance - amount 
     return
   }
}
