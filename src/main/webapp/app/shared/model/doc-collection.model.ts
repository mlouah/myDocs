import { IDoc } from '@/shared/model/doc.model';
import { IDocPublisher } from '@/shared/model/doc-publisher.model';

export interface IDocCollection {
  id?: number;
  name?: string | null;
  notes?: string | null;
  docs?: IDoc[] | null;
  docPublisher?: IDocPublisher | null;
}

export class DocCollection implements IDocCollection {
  constructor(
    public id?: number,
    public name?: string | null,
    public notes?: string | null,
    public docs?: IDoc[] | null,
    public docPublisher?: IDocPublisher | null
  ) {}
}
