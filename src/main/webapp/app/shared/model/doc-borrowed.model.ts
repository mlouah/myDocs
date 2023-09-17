import { IDoc } from '@/shared/model/doc.model';

export interface IDocBorrowed {
  id?: number;
  borrowDate?: Date | null;
  borrowerName?: string | null;
  notes?: string | null;
  doc?: IDoc | null;
}

export class DocBorrowed implements IDocBorrowed {
  constructor(
    public id?: number,
    public borrowDate?: Date | null,
    public borrowerName?: string | null,
    public notes?: string | null,
    public doc?: IDoc | null
  ) {}
}
