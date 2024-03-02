import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";
export class GameMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.L = 0;

    this.cols = 15;
    this.rows = 16;
    this.store = store;

    this.walls = []
    this.inner_wall_count = 40;

    this.snakes = [
      new Snake({ id: 0, color: "#ffe898", r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: "#ff8d95", r: 1, c: this.cols - 2 }, this)
    ];
  }

  add_listening_events () {
    this.ctx.canvas.focus();

    const [snake0, snake1] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", e => {
      if (e.key === "w") snake0.set_direction(0);
      else if (e.key === "d") snake0.set_direction(1);
      else if (e.key === "s") snake0.set_direction(2);
      else if (e.key === "a") snake0.set_direction(3);
      else if (e.key === "ArrowUp") snake1.set_direction(0);
      else if (e.key === "ArrowRight") snake1.set_direction(1);
      else if (e.key === "ArrowDown") snake1.set_direction(2);
      else if (e.key === "ArrowLeft") snake1.set_direction(3);
    });
  }

  start () {
    this.create_obstacles();
    this.add_listening_events();
  }

  // check_connectivity(g, sx, sy, tx, ty) {
  //     if (sx == tx && sy == ty) return true;
  //     g[sx][sy] = true;

  //     let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
  //     for (let i = 0; i < 4; i++) {
  //         let x = sx + dx[i], y = sy + dy[i];
  //         if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
  //             return true;
  //     }
  //     return false;
  // }

  check_ready () {
    //判断两条蛇是否准备好下一回合
    for (const snake of this.snakes) {
      if (snake.status !== "idle") return false;
      if (snake.direction === -1) return false;
    }
    return true;
  }

  create_obstacles () {
    const g = this.store.state.pk.gamemap;
    //console.log("g" + g)

    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        //console.log("g_r"+r+" g_c"+c+" :"+g[r][c])
        if (g[r][c])
          this.walls.push(new Wall(r, c, this));
      }
    }
    return true;
  }

  update_size () {
    //clientWidth是求div的长宽的函数
    //不要缝隙就用parseInt，因为canvs是按整数画的
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    //this.L=Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows);
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  next_step () {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }
  //检测目标位置是否合法，没有撞到蛇或者墙
  check_valid (cell) {
    for (const wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c)
        return false;
    }
    for (const snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) {
        k--;
      }
      //当蛇会前进时，不需要判断
      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
          return false;
      }
    }
    return true;
  }

  update () {
    this.update_size();
    if (this.check_ready()) {
      console.log("更新")
      this.next_step();
    }

    this.render();
  }
  //渲染
  render () {
    const color_even = "#f1ddff", color_odd = "#f1edff"
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 1) this.ctx.fillStyle = color_even;
        else this.ctx.fillStyle = color_odd;
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
      }
    }
    // this.ctx.fillStyle="green";
    // this.ctx.fillRect(0,0,this.ctx.canvas.width,this.ctx.canvas.height);

  }
}
