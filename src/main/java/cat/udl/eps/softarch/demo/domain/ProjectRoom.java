package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "project_room")
@Getter
@Setter
public class ProjectRoom {

	@Id
	@Column(name = "room_number")
	private String roomNumber;

	@OneToOne
	@JoinColumn(name = "managed_by_judge_id")
	private Judge managedByJudgeId;

}
