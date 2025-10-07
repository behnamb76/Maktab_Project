package ir.maktabsharif.finalproject.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionBank extends BaseModel {
    @OneToOne
    private Course course;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
}
