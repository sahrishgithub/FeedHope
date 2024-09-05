package com.example.unitconverter.ProviderInterface;

public class ProvideFoodModalClass {
        private String food,quantity,storage,available,expire;
        public String getFood() { return food; }
        public void setFood(String food) {this.food = food;}
        public String getQuantity()
        {
            return quantity;
        }
        public void setQuantity(String quantity) {this.quantity = quantity;}
        public String getStorage() { return storage; }
        public void setStorage(String storage)
        {
            this.storage = storage;
        }
        public String getAvailable()
        {
            return available;
        }
        public void setAvailable(String available) {this.available = available;}
        public String getExpire() { return expire; }
        public void setExpire(String expire) { this.expire = expire; }
        public ProvideFoodModalClass(String food,
                           String quantity,
                           String storage,
                           String available,
                           String expire)
        {
            this.food = food;
            this.quantity = quantity;
            this.storage = storage;
            this.available = available;
            this.expire = expire;
        }
}