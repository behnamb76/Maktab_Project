package ir.maktabsharif.finalproject.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
