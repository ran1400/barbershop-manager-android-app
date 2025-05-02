package com.example.barbershopmanager.utils.dataStructures;

public class User
{
    public String name;
    public String phone;
    public String mail;
    public String queue;
    public boolean blocked;


    public User(String name,String phone,String mail,String queue,boolean blocked)
    {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.queue = queue;
        this.blocked = blocked;
    }

}
