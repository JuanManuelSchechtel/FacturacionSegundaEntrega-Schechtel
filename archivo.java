import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoApplication.class, args);
    }
}

abstract class Vehiculo {
    protected String marca;
    protected String modelo;

    public Vehiculo(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }

    public abstract String obtenerDetalles();
}

@Entity
class Auto extends Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Auto(String marca, String modelo) {
        super(marca, modelo);
    }

    @Override
    public String obtenerDetalles() {
        return "Auto: " + marca + " " + modelo;
    }

    public Long getId() {
        return id;
    }
}

interface AutoRepository extends JpaRepository<Auto, Long> {}

@Service
class AutoService {
    @Autowired
    private AutoRepository autoRepository;

    public List<Auto> obtenerTodosLosAutos() {
        return autoRepository.findAll();
    }

    public Auto agregarAuto(Auto auto) {
        return autoRepository.save(auto);
    }

    public Auto obtenerAutoPorId(Long id) throws Exception {
        return autoRepository.findById(id)
                .orElseThrow(() -> new Exception("Auto no encontrado"));
    }
}

@RestController
@RequestMapping("/autos")
class AutoController {
    @Autowired
    private AutoService autoService;

    @GetMapping
    public List<Auto> listarAutos() {
        return autoService.obtenerTodosLosAutos();
    }

    @PostMapping
    public Auto crearAuto(@RequestBody Auto auto) {
        return autoService.agregarAuto(auto);
    }

    @GetMapping("/{id}")
    public Auto obtenerAuto(@PathVariable Long id) throws Exception {
        return autoService.obtenerAutoPorId(id);
    }
}