import { AcGameObject } from './AcGameObject';
import { Cell } from "./Cell"

export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        //存放蛇的身体，cell0是蛇头
        this.cells = [new Cell(info.r, info.c)];

        this.next_cell = null;    //下一步的目标位置
        this.speed = 5; //蛇每秒走5个格子
        this.direction = -1;  //-1表示没有指令，0123表示上右下左；
        this.status = "idle"; //idle表示静止状态，move表示正在移动，die表示失败；
        this.dr = [-1, 0, 1, 0]; //4个方向行偏移量
        this.dc = [0, 1, 0, -1]; //4个方向列的偏移量

        this.step = 0; //表示回合数
        this.eps = 1e-2; //允许的误差。当两点相差eps时，认为两点重合

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2; //左下角的蛇初始朝上，右下角的蛇初始朝下
        //蛇有两只眼镜，dx相当于两只眼睛相对于canvas应该加或减的偏移量
        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [-1, 1],
            [-1, -1]
        ];
        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1]
        ];
    }



    start() {

    }

    //检测是否增长
    check_tail_increasing() {
        if (this.step <= 5) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }

    set_direction(d) {
        this.direction = d;
    }

    next_step() {
        //将蛇的状态变为走下一步；
        const d = this.direction;
        this.eye_direction = d;
        console.log(this.id + "方向:" + d);
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        // console.log(this.id + "下一点:" + this.next_cell.x + " " + this.next_cell.y)
        this.direction = -1;
        this.status = "move"
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
        //代表下一步会撞墙了，直接结束
        if (!this.gamemap.check_valid(this.next_cell)) {
            this.status = "die";
        }


    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < this.eps) {
            this.cells[0] = this.next_cell;   //添加一个新蛇头
            this.next_cell = null;
            this.status = "idle"; //走完了，停止

            //蛇不变长，去除蛇尾
            if (!this.check_tail_increasing()) {
                this.cells.pop()
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000;   //每两帧之间走的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance
            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2]
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }

    }

    //每一帧执行一次
    update() {
        if (this.status === "move") {
            this.update_move();
        }
        this.render();
    }
    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        if (this.status === "die")
            this.color = "#fff"

        ctx.fillStyle = this.color;
        for (const cell of this.cells) {
            ctx.beginPath();
            //console.log(""+cell.x+" "+cell.y)
            //注意这里y代表的是row，x代表的是col
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1];
            const b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = "#000";
        for (let i = 0; i < 2; i++) {
            // const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction[i]] * 0.25* L) ;
            // const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction[i]] * 0.25* L) ;
            const eye_x = (this.cells[0].x+this.eye_dx[this.eye_direction][i]*0.15)*L ;
            const eye_y = (this.cells[0].y+this.eye_dy[this.eye_direction][i]*0.15)*L  ;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, 0.05*L , 0, Math.PI * 2);
            ctx.fill();
        }
    }

}