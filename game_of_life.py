import sys

class InvalidBoard(Exception):
    pass

class Board:
    def __init__(self, filename):
        with open(filename, 'r') as f:
            lines = f.readlines()

        self.board = []
        for line in lines:
            row = [c == 'O' for c in line.strip()]
            self.board.append(row)

        if not all(len(row) == len(self.board[0]) for row in self.board):
            raise InvalidBoard()

    def next_generation(self):
        new_board = [[False]*len(row) for row in self.board]
        for i in range(len(self.board)):
            for j in range(len(self.board[0])):
                live_neighbors = sum(self.board[i+di][j+dj] for di in [-1, 0, 1] for dj in [-1, 0, 1] 
                                     if 0 <= i+di < len(self.board) and 0 <= j+dj < len(self.board[0]) 
                                     and not (di == dj == 0))
                if self.board[i][j] and live_neighbors in [2, 3]:
                    new_board[i][j] = True
                elif not self.board[i][j] and live_neighbors == 3:
                    new_board[i][j] = True
        self.board = new_board

    def __str__(self):
        return '\n'.join(''.join('O' if cell else '.' for cell in row) for row in self.board)

    def __eq__(self, other):
        return isinstance(other, Board) and self.board == other.board

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 game_of_life.py <file> <generations>")
        sys.exit(1)
    try:
        board = Board(sys.argv[1])
        generations = int(sys.argv[2])
        for _ in range(generations):
            print(board)
            board.next_generation()
    except InvalidBoard:
        print("Invalid board file")
