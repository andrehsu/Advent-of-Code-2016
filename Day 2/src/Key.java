/**
 * Created by Andre on 12/5/2016.
 */
abstract class Key {
	abstract Key up();
	
	abstract Key down();
	
	abstract Key left();
	
	abstract Key right();
	
	abstract char value();
	
	protected class ValueOutOfBoundsException extends RuntimeException {
		ValueOutOfBoundsException() {
			super();
		}
		
		ValueOutOfBoundsException(String message) {
			super(message);
		}
	}
}

final class Type1 extends Key {
	private final int row, column;
	
	Type1() {
		this(1, 1);
	}
	
	private Type1(int row, int column) {
		if (row > 2 || row < 0 || column > 2 || column < 0) {
			throw new ValueOutOfBoundsException();
		}
		this.row = row;
		this.column = column;
	}
	
	@Override
	Type1 up() {
		if (row > 0)
			return new Type1(row - 1, column);
		else
			return this;
		
	}
	
	@Override
	Type1 down() {
		if (row < 2)
			return new Type1(row + 1, column);
		else
			return this;
		
	}
	
	@Override
	Type1 left() {
		if (column > 0)
			return new Type1(row, column - 1);
		else
			return this;
	}
	
	@Override
	Type1 right() {
		if (column < 2)
			return new Type1(row, column + 1);
		else
			return this;
	}
	
	@Override
	char value() {
		switch (row) {
			case 0:
				switch (column) {
					case 0:
						return Character.forDigit(1, 10);
					case 1:
						return Character.forDigit(2, 10);
					case 2:
						return Character.forDigit(3, 10);
				}
			case 1:
				switch (column) {
					case 0:
						return Character.forDigit(4, 10);
					case 1:
						return Character.forDigit(5, 10);
					case 2:
						return Character.forDigit(6, 10);
				}
			case 2:
				switch (column) {
					case 0:
						return Character.forDigit(7, 10);
					case 1:
						return Character.forDigit(8, 10);
					case 2:
						return Character.forDigit(9, 10);
				}
		}
		throw new ValueOutOfBoundsException();
	}
}

final class Type2 extends Key {
	private final int row, column;
	
	Type2() {
		this(2, 0);
	}
	
	private Type2(int row, int column) {
		validate(row, column);
		
		this.row = row;
		this.column = column;
	}
	
	@Override
	Key up() {
		try {
			return new Type2(row - 1, column);
		} catch (ValueOutOfBoundsException e) {
			return this;
		}
	}
	
	@Override
	Key down() {
		try {
			return new Type2(row + 1, column);
		} catch (ValueOutOfBoundsException e) {
			return this;
		}
	}
	
	@Override
	Key left() {
		try {
			return new Type2(row, column - 1);
		} catch (ValueOutOfBoundsException e) {
			return this;
		}
	}
	
	@Override
	Key right() {
		try {
			return new Type2(row, column + 1);
		} catch (ValueOutOfBoundsException e) {
			return this;
		}
	}
	
	@Override
	char value() {
		switch (row) {
			case 0:
				return 1;
			case 1:
				switch (column) {
					case 1:
						return '2';
					case 2:
						return '3';
					case 3:
						return '4';
				}
			case 2:
				switch (column) {
					case 0:
						return '5';
					case 1:
						return '6';
					case 2:
						return '7';
					case 3:
						return '8';
					case 4:
						return '9';
				}
			case 3:
				switch (column) {
					case 1:
						return 'A';
					case 2:
						return 'B';
					case 3:
						return 'C';
				}
			case 4:
				return 'D';
		}
		throw new ValueOutOfBoundsException();
	}
	
	private void validate(int row, int column) {
		switch (row) {
			case 0:
				if (column != 2) {
					throw new ValueOutOfBoundsException();
				}
				break;
			case 1:
				switch (column) {
					case 1:
					case 2:
					case 3:
						break;
					default:
						throw new ValueOutOfBoundsException();
				}
				break;
			case 2:
				switch (column) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						break;
					default:
						throw new ValueOutOfBoundsException();
				}
				break;
			case 3:
				switch (column) {
					case 1:
					case 2:
					case 3:
						break;
					default:
						throw new ValueOutOfBoundsException();
				}
				break;
			case 4:
				if (column != 2) {
					throw new ValueOutOfBoundsException();
				}
				break;
			default:
				throw new ValueOutOfBoundsException();
		}
	}
}