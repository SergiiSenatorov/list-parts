package com.nodomain.listparts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Controller
public class MainController
{
    private final PartRepository partRepository;

    private static final int RECORDS_BY_PAGE = 10;
    private static final String SORT = "name";

    @Autowired

    public MainController(PartRepository partRepository)
    {
        this.partRepository = partRepository;
    }

    // "Корень"
    @GetMapping("/")
    public String parts
    (
       @RequestParam(value = "q", required = false) String query,
       @RequestParam(value = "necessary", required = false) Boolean necessary,
       Model model,
       @PageableDefault(size = RECORDS_BY_PAGE, sort = SORT, direction = ASC) Pageable pageable
    )
    {
        Page<Part> parts = partRepository.findByName((query != null) ? query : "", necessary, pageable);
        model.addAttribute("parts", parts);                 // Комплектующие
        model.addAttribute("query", query);                 // Запрос
        model.addAttribute("necessary",necessary);          // Признак 'необходимости'
        model.addAttribute("pccount",computersAmount());    // Кол-во ПК к сборке

        // Пейджинг
        model.addAttribute("totalPages", parts.getTotalPages());
        model.addAttribute("current", pageable.getPageNumber());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        return "parts";
    }

    // 'Детализация' по комплектующему
    @GetMapping("/part/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        model.addAttribute("part", partRepository.getOne(id));
        return "edit";
    }

    @PostMapping("/part")
    public String save(@ModelAttribute Part part)
    {
        partRepository.save(part);
        try
        {
            return "redirect:/?q=" + URLEncoder.encode(part.getName(), "UTF8");
        }
        catch (UnsupportedEncodingException ignore)
        {
            return "redirect:/";
        }
    }

    @GetMapping("/part/delete/{id}")
    public String delete(@PathVariable Long id)
    {
        partRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/part")
    public String create(Model model)
    {
        Part part = new Part();
        model.addAttribute("part", part);
        return "edit";
    }

    // Подсчет ПК, готовых к сборке (min по выборке)
    private Long computersAmount()
    {
        List<Part> list = partRepository.findAllNecessary();
        Long pccount = 0L;
        if(!list.isEmpty())
            pccount = list.get(0).getAmount();
        return pccount;
    }
}
