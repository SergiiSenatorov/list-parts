package com.nodomain.listparts;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

// Выборка для подсчета ПК, готовых к сборке
@NamedQuery(name = "Part.findAllNecessary", query="select p from Part p where p.necessary=TRUE order by p.amount")

public class Part
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;                // ИН, автогенерация

    private String name;            // Наименование

    private Boolean necessary;      // Признак "необходимости"

    private Long amount;            // Кол-во на складе

    protected Part()
    {
        // Ничего не делает, нужен для корректной отработки Spring'а
    }

    public Part(String name, Boolean necessary, Long amount)
    {
        this.name = name;
        this.necessary = necessary;
        this.amount = amount;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getNecessary()
    {
        return necessary;
    }

    public void setNecessary(Boolean necessary)
    {
        this.necessary = necessary;
    }

    public Long getAmount()
    {
        return amount;
    }

    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return "Part{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isNecessary=" + necessary +
                ", amount=" + amount +
                '}';
    }
}
